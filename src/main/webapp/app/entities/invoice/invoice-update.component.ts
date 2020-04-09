import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { IInvoice, Invoice } from 'app/shared/model/invoice.model';
import { InvoiceService } from './invoice.service';
import { ItemsService } from '../items/items.service';
import { Items } from 'app/shared/model/items.model';
import { PettyCashStockService } from '../petty-cash-stock/petty-cash-stock.service';
import { IPettyCashStock } from 'app/shared/model/petty-cash-stock.model';
import { DailyLogService } from '../daily-log/daily-log.service';
import { IDailyLog, DailyLog } from 'app/shared/model/daily-log.model';
import { CustomerService } from '../customer/customer.service';
import { ICustomer, Customer } from 'app/shared/model/customer.model';
import { Moment } from 'moment';

@Component({
  selector: 'jhi-invoice-update',
  templateUrl: './invoice-update.component.html'
})
export class InvoiceUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    customerId: [],
    rx: [],
    deleveryDate: [],
    orderDate: [],
    advance: [],
    balance: [],
    delivered: [],
    paid: []
  });
  itemsList = [{}];
  oldValue: any = -99;
  currentSearch: string;
  predicate = '';
  reverse: any;
  pettyCashStockList: IPettyCashStock[];
  customer: ICustomer;
  constructor(protected invoiceService: InvoiceService,
    protected itemsService: ItemsService,
    protected activatedRoute: ActivatedRoute,
    private pettyCashStockService: PettyCashStockService,
    private customerService: CustomerService,
    private router: Router,
    private dailyLogService: DailyLogService,
    private fb: FormBuilder) { }

  ngOnInit() {
    // this.customer=new Customer();
    this.reverse = false;
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ invoice }) => {
      this.updateForm(invoice);
    });

    this.activatedRoute.params.subscribe(params => {
      if (typeof params['customerId'] !== 'undefined') {
        this.editForm.patchValue({
          customerId: params['customerId']

        });

        this.customerService.find(params['customerId']).subscribe(res => {
          this.customer = res.body
        })
      }

      // In a real app: dispatch action to load the details here.
    });
    this.initItemsTable();
    this.oldValue = this.editForm.get('advance').value;
    this.editForm.valueChanges.subscribe(res => {


      if (this.oldValue !== res.advance) {
        // alert(res.advance + ' ' + this.oldValue)
        this.oldValue = res.advance;
        let tot = 0;
        this.itemsList.forEach((d) => {
          tot += Number(d['amount']);
        });
        const balance = tot - Number(res.advance);
        this.editForm.patchValue({
          balance: Number(balance)

        });

      }
    });

    this.invoiceService
      .search({
        page: 0,
        query: '*',
        size: 1,
        sort: this.sort()
      })
      .subscribe((res) => {

        if (res.body.length > 0) {
          this.editForm.patchValue({
            rx: typeof Number(res.body[0]['rx']) === 'undefined' ? 0 : Number(res.body[0].rx) + 1

          });
        } else { 
          this.editForm.patchValue({
            rx: 0

          });
        }

      });


    this.pettyCashStockService.query().subscribe(res => {
      this.pettyCashStockList = res.body;
    });


  }
  onFormChange() {

  }


  updateForm(invoice: IInvoice) {
    this.editForm.patchValue({
      id: invoice.id,
      customerId: invoice.customerId,
      rx: invoice.rx,
      deleveryDate: invoice.deleveryDate != null ? invoice.deleveryDate.format(DATE_TIME_FORMAT) : null,
      orderDate: invoice.orderDate != null ? invoice.orderDate.format(DATE_TIME_FORMAT) : null,
      advance: invoice.advance,
      balance: invoice.balance,
      delivered: invoice.delivered,
      paid: invoice.paid
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const invoice = this.createFromForm();
    if (invoice.id !== undefined) {
      this.subscribeToSaveResponse(this.invoiceService.update(invoice));
    } else {
      this.subscribeToSaveResponse(this.invoiceService.create(invoice));
    }
  }

  private createFromForm(): IInvoice {
    return {
      ...new Invoice(),
      id: this.editForm.get(['id']).value,
      customerId: this.editForm.get(['customerId']).value,
      rx: this.editForm.get(['rx']).value,
      deleveryDate:
        this.editForm.get(['deleveryDate']).value != null ? moment(this.editForm.get(['deleveryDate']).value, DATE_TIME_FORMAT) : undefined,
      orderDate:
        this.editForm.get(['orderDate']).value != null ? moment(this.editForm.get(['orderDate']).value, DATE_TIME_FORMAT) : undefined,
      advance: this.editForm.get(['advance']).value,
      balance: this.editForm.get(['balance']).value,
      delivered: this.editForm.get(['delivered']).value,
      paid: this.editForm.get(['paid']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvoice>>) {
    result.subscribe((data) => {
      this.customerService.find(this.editForm.get(['customerId']).value).subscribe(res => {
        this.customer = res.body

        this.onSaveSuccess(data)
      })
    }, () => this.onSaveError());
  }

  protected onSaveSuccess(data) {
    this.isSaving = false;

    if (this.editForm.get('id').value === null) {
      // alert('Create new item')
      this.itemsList.forEach((d) => {
        const item = new Items();
        item.amount = d['amount'];
        item.invoiceId = data.body.id;
        item.description = d['description'];
        item.quantity = d['quantity'];
        item.rate = d['rate'];
        this.itemsService.create(item).subscribe(() => { });
        this.updatePettyCashStockQty(d)
      });

    } else {
      this.itemsList.forEach((d) => {
        const item = new Items();
        item.id = d['id'];
        item.amount = d['amount'];
        item.invoiceId = data.body.id;
        item.description = d['description'];
        item.quantity = d['quantity'];
        item.rate = d['rate'];
        if (typeof item.id === 'undefined') {
          this.itemsService.create(item).subscribe(() => { });
          this.updatePettyCashStockQty(d)
        } else {
          this.itemsService.update(item).subscribe(() => { });
          // this.updatePettyCashStockQty()
        }
      });
    }

  //   this.dailyLogService.search({ query: 'invoiceId:' + data.body['id'] }).subscribe(res => {
  //     alert(res.body)
  //     if (res.body) {
      
  //  this. insertRecordToDailyLog(data);
  //     } else {
  //       // 
  //       alert('Not Empty, already has a record')
  //     }
  //   })

  this. insertRecordToDailyLog(data);

    // this.router.navigate(['print/invoice/' + data.body.id])
    this.previousState();
  }
  insertRecordToDailyLog(data) {


    const rx = Number(data.body['rx']);
    const invoiceId = data.body['id'];
    const customerDetails = this.customer.title + ' ' + this.customer.firstName + ' ' + this.customer.lastName;
    let description = '';
    let amount = 0;
    const created: Moment = moment(moment().format('YYYY-MM-DDThh:MM'))
    const dailyLog = new DailyLog();
    dailyLog.customerId = this.customer.id;
    if (this.editForm.get(['paid']).value && Number(this.editForm.get(['balance']).value) === 0) {
      description = 'FULL_PAID'
      amount = this.editForm.get(['advance']).value;
      dailyLog.rx = rx;
      dailyLog.amount = amount;
      dailyLog.invoiceId = invoiceId;
      dailyLog.customer = customerDetails;
      dailyLog.description = description;
      dailyLog.createdDate = created;
      this.dailyLogService.create(dailyLog).subscribe(res => {
        // 
      })

    }

    else if (!this.editForm.get(['paid']).value && Number(this.editForm.get(['balance']).value) > 0) {// save advance
      description = 'ADVANCE'
      amount = this.editForm.get(['advance']).value;
      dailyLog.rx = rx;
      dailyLog.amount = amount;
      dailyLog.invoiceId = invoiceId;
      dailyLog.customer = customerDetails;
      dailyLog.description = description;
      dailyLog.createdDate = created;
      this.dailyLogService.create(dailyLog).subscribe(res => {
        // 
      })
    }

    else if (this.editForm.get(['paid']).value && Number(this.editForm.get(['balance']).value) > 0) {// save balance
      description = 'BALANCE'
      amount = this.editForm.get(['balance']).value;
      dailyLog.rx = rx;
      dailyLog.amount = amount;
      dailyLog.invoiceId = invoiceId;
      dailyLog.customer = customerDetails;
      dailyLog.description = description;
      dailyLog.createdDate = created;

      this.dailyLogService.create(dailyLog).subscribe(res => {
        // 
      })
    }


  }



  updatePettyCashStockQty(data) {
    this.pettyCashStockList.forEach(d => {
      const items = String(data['description']).split('|');
      // alert(JSON.stringify(items))
      if (String(d.modelNumber).trim() === String(items[1].trim())) {
        // alert(d.modelNumber)
        // 
        const params = new URLSearchParams();
        params.append('modelNumber', d.modelNumber);
        const query = { query: params };
        this.pettyCashStockService.search(query).subscribe(res => {
          //
          const pettyItem: IPettyCashStock = res.body[0]
          // alert(JSON.stringify(res))
          pettyItem.quantity = Number(pettyItem.quantity) - Number(data['quantity']);
          this.pettyCashStockService.update(pettyItem).subscribe(() => {

          })
        })
      }

    });

  }


  protected onSaveError() {
    this.isSaving = false;
  }

  initItemsTable() {
    if (this.editForm.get('id').value) {
      const params = new URLSearchParams();
      params.append('invoiceId', this.editForm.get('id').value);
      const query = { query: params };
      this.itemsService.search(query).subscribe(res => {
        this.itemsList.pop();
        res.body.forEach((d) => {
          this.itemsList.push({
            id: d.id,
            quantity: d.quantity,
            description: d.description,
            rate: d.rate,
            amount: d.amount
          });
        });
      });

    }

  }
  addRow() {
    this.itemsList.push({})
  }
  removeRow(i) {
    this.itemsService.delete(this.itemsList[i]['id']).subscribe(() => {
      this.itemsList.splice(i, 1);
    })
  }
  show() {
    alert(JSON.stringify(this.itemsList))
  }
  sort() {
    const result = ['id' + ',' + (this.reverse ? 'asc' : 'desc')];

    return result;
  }
  onTableFormChange(i) {
    const rate = this.itemsList[i]['rate'];
    const qty = this.itemsList[i]['quantity'];
    this.itemsList[i]['amount'] = Number(rate) * Number(qty);
  }
  calcTotal() {
    let tot = 0;
    this.itemsList.forEach((d) => {
      tot += Number(d['amount']);
    });
    this.editForm.patchValue({
      advance: Number(tot)

    });

  }

  exchangeValues() {
    const advance = this.editForm.get(['advance']).value;
    const balance = this.editForm.get(['balance']).value;

    this.editForm.patchValue({
      advance: Number(balance),
      balance: Number(advance),

    });

  }

  onPaid() {

  }

  onInputItem(item, index) {
    // alert("Input "+ JSON.stringify(item))

    const items = String(item['description']).split('|');
    if (items.length > 1) {
      const params = new URLSearchParams();
      params.append('modelNumber', String(items[1].trim()));
      const query = { query: params };
      this.pettyCashStockService.search(query).subscribe(res => {
        item['rate'] = res.body[0].price;

        this.onTableFormChange(index)
      });

    }

  }

  nowDateTimeOrder(){
    this.editForm.patchValue(
      {
        orderDate:moment().format('YYYY-MM-DDThh:MM')
      }
    )
  }
  nowDateTimeDelivery(){
    this.editForm.patchValue(
      {
        deleveryDate:moment().format('YYYY-MM-DDThh:MM')
      }
    )
  }

  addDays(){
    this.editForm.patchValue(
      {
        deleveryDate:  moment(   moment(this.editForm.get(['deleveryDate']).value ).add(1,'days')).format('YYYY-MM-DDThh:MM')
      }
    )
  }
}
