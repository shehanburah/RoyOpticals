import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { ISolderingJobInvoice, SolderingJobInvoice } from 'app/shared/model/soldering-job-invoice.model';
import { SolderingJobInvoiceService } from './soldering-job-invoice.service';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from '../customer/customer.service';
import { DailyLog } from 'app/shared/model/daily-log.model';
import { DailyLogService } from '../daily-log/daily-log.service';

@Component({
  selector: 'jhi-soldering-job-invoice-update',
  templateUrl: './soldering-job-invoice-update.component.html'
})
export class SolderingJobInvoiceUpdateComponent implements OnInit {
  isSaving: boolean;
  orderDateDp: any;
  deliveryDateDp: any;

  editForm = this.fb.group({
    id: [],
    customerId: [],
    price: [],
    orderDate: [],
    delivered: [],
    deliveryDate: []
  });

  customer: ICustomer;

  constructor(
    protected solderingJobInvoiceService: SolderingJobInvoiceService,
    protected activatedRoute: ActivatedRoute,
    private customerService: CustomerService,
    private fb: FormBuilder,
    private dailyLogService:DailyLogService
  ) { }

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ solderingJobInvoice }) => {
      this.updateForm(solderingJobInvoice);
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
    });
  }

  updateForm(solderingJobInvoice: ISolderingJobInvoice) {
    this.editForm.patchValue({
      id: solderingJobInvoice.id,
      customerId: solderingJobInvoice.customerId,
      price: solderingJobInvoice.price,
      orderDate: solderingJobInvoice.orderDate,
      delivered: solderingJobInvoice.delivered,
      deliveryDate: solderingJobInvoice.deliveryDate
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const solderingJobInvoice = this.createFromForm();
    if (solderingJobInvoice.id !== undefined) {
      this.subscribeToSaveResponse(this.solderingJobInvoiceService.update(solderingJobInvoice));
    } else {
      this.subscribeToSaveResponse(this.solderingJobInvoiceService.create(solderingJobInvoice));
    }
  }

  private createFromForm(): ISolderingJobInvoice {
    return {
      ...new SolderingJobInvoice(),
      id: this.editForm.get(['id']).value,
      customerId: this.editForm.get(['customerId']).value,
      price: this.editForm.get(['price']).value,
      orderDate: this.editForm.get(['orderDate']).value,
      delivered: this.editForm.get(['delivered']).value,
      deliveryDate: this.editForm.get(['deliveryDate']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISolderingJobInvoice>>) {
    result.subscribe((data) => {
      this.customerService.find(this.editForm.get(['customerId']).value).subscribe(res => {
        this.customer = res.body

        this.onSaveSuccess(data)
      });

    }, () => this.onSaveError());
  }

  protected onSaveSuccess(data) {

    const invoiceId = data.body['id'];
    const customerDetails = this.customer.title + ' ' + this.customer.firstName + ' ' + this.customer.lastName;
    let description = '';
    let amount = 0;
    const created: moment.Moment = moment(moment().format('YYYY-MM-DDThh:MM'))
    const dailyLog = new DailyLog();

    dailyLog.customerId = this.customer.id;
    description = 'SOLDERING_FULL_PAID'
    amount = this.editForm.get(['price']).value; 
    dailyLog.amount = amount;
    dailyLog.solderingJobInvoiceId = invoiceId;
    dailyLog.customer = customerDetails;
    dailyLog.description = description;
    dailyLog.createdDate = created;
    this.dailyLogService.create(dailyLog).subscribe(res => {
      // 
    })

    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
