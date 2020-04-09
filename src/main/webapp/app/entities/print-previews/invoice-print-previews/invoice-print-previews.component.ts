import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IInvoice } from 'app/shared/model/invoice.model';
import { ItemsService } from 'app/entities/items/items.service';
import { CustomerService } from 'app/entities/customer/customer.service';
import { ICustomer, Customer } from 'app/shared/model/customer.model';
import { InvoiceService } from 'app/entities/invoice/invoice.service';

@Component({
  selector: 'jhi-print-previews',
  templateUrl: './invoice-print-previews.component.html',
  styleUrls: ['./invoice-print-previews.component.scss']
})
export class InvoicePrintPreviewsComponent implements OnInit {
  invoice: IInvoice;
  groups = [];
  invoiceId: '';
  readyToPrint = true;
  customerDetails:ICustomer=new Customer();
  maxLines=5;
  negombo=false;
  dankotuwa=false;
  wennapuwa=false;

  constructor(private activatedRoute: ActivatedRoute,
     private itemsService: ItemsService,
      private customerService: CustomerService,
      private invoiceService: InvoiceService
      ) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ invoice }) => {
      this.invoice = invoice;
      this.invoiceId = invoice.id;
      this.readyToPrint = false;
 

      const params = new URLSearchParams(); 
      params.append('invoiceId', invoice.id);
      const query = { query: params };
      this.itemsService.search(query).subscribe(res => {
        res.body.forEach((d) => {
          this.groups.push({
            id: d.id,
            quantity: d.quantity,
            description: d.description,
            rate: d.rate,
            amount: d.amount
          });
        });
      });
      this.customerService.find(invoice.customerId).subscribe(data => {
        this.customerDetails = data.body;
      });
    });

    //
  }
}
