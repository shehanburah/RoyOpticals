import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISolderingJobInvoice } from 'app/shared/model/soldering-job-invoice.model';

@Component({
  selector: 'jhi-soldering-job-invoice-detail',
  templateUrl: './soldering-job-invoice-detail.component.html'
})
export class SolderingJobInvoiceDetailComponent implements OnInit {
  solderingJobInvoice: ISolderingJobInvoice;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ solderingJobInvoice }) => {
      this.solderingJobInvoice = solderingJobInvoice;
    });
  }

  previousState() {
    window.history.back();
  }
}
