import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Route, Router } from '@angular/router';
import * as moment from 'moment';
import { ICustomer } from 'app/shared/model/customer.model';
import { InvoiceService } from '../invoice/invoice.service';
import { PrescriptionService } from '../prescription/prescription.service';
import { DescriptionService } from '../description/description.service';
import { SolderingJobInvoiceService } from '../soldering-job-invoice/soldering-job-invoice.service';
import { ISolderingJobInvoice } from 'app/shared/model/soldering-job-invoice.model';
// declare let moment: any;

@Component({
  selector: 'jhi-customer-detail',
  templateUrl: './customer-detail.component.html'
})
export class CustomerDetailComponent implements OnInit {
  customer: ICustomer;
  invoicesNprescriptions = [];
  array: string;
  solderingInvoices: ISolderingJobInvoice[];

  constructor(protected activatedRoute: ActivatedRoute,
    protected invoiceService: InvoiceService,
    protected prescriptionService: PrescriptionService,
    protected descriptionService: DescriptionService,
    protected solderingJobInvoiceService: SolderingJobInvoiceService,
    private router: Router

  ) { }

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ customer }) => {
      this.customer = customer;
      this.getInvoices(customer);
    });

  }
  getInvoices(customer) {
    const params = new URLSearchParams();
    params.append('customerId', customer.id);
    const query = { query: params };

    this.invoiceService.search(query).subscribe(res => {
      res.body.forEach((d) => {

        const params2 = new URLSearchParams();
        params2.append('invoiceId', d.id);
        const query2 = { query: params2 };

        this.descriptionService.search(query2).subscribe(desRes => {
          this.prescriptionService.search(query2).subscribe(presRes => {
            // alert( new Date( String( moment(d.orderDate).format('YYYY-MM-DD') )  ).getTime())
            this.invoicesNprescriptions.push({
              id: d.id,
              rx: d.rx,
              orderDate: d.orderDate,
              delivered: d.delivered,
              deleveryDate: d.deleveryDate,
              paid: d.paid,
              descriptions: desRes.body,
              prescriptions: presRes.body
            });
            // alert(d.orderDate)
            this.invoicesNprescriptions = this.invoicesNprescriptions.sort(this.customSortRx)
          });

          this.invoicesNprescriptions = this.invoicesNprescriptions.sort(this.customSortRx)
        });
        this.invoicesNprescriptions = this.invoicesNprescriptions.sort(this.customSortRx)
      });
      this.invoicesNprescriptions = this.invoicesNprescriptions.sort(this.customSortRx)

    });
    this.solderingJobInvoiceService.search(query).subscribe(res => {
      this.solderingInvoices = res.body;
      this.sortNowSoldering()
      // alert(JSON.stringify(this.solderingInvoices))
    });
  }
  customSort(b, a) {

    return new Date(String(moment(a.orderDate).format('YYYY-MM-DD'))).getTime() - new Date(String(moment(b.orderDate).format('YYYY-MM-DD'))).getTime();
  }
  customSortRx(b, a) {

    return a.rx - b.rx;
  }
  previousState() {
    window.history.back();
  }
  convert(str) {
    const date = new Date(str),
      mnth = ("0" + (date.getMonth() + 1)).slice(-2),
      day = ("0" + date.getDate()).slice(-2);
    return [date.getFullYear(), mnth, day].join("-");
  }
  sortNow() {
    this.invoicesNprescriptions = this.invoicesNprescriptions.sort(this.customSortRx)
  }
  sortNowSoldering() {

    this.solderingInvoices = this.solderingInvoices.sort(this.customSort)
  }
  printThisInvoice(id) {
    this.router.navigate(['print/invoice', id])
  }
}
