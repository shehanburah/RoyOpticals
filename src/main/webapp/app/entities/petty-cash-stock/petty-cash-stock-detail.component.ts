import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPettyCashStock } from 'app/shared/model/petty-cash-stock.model';

@Component({
  selector: 'jhi-petty-cash-stock-detail',
  templateUrl: './petty-cash-stock-detail.component.html'
})
export class PettyCashStockDetailComponent implements OnInit {
  pettyCashStock: IPettyCashStock;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ pettyCashStock }) => {
      this.pettyCashStock = pettyCashStock;
    });
  }

  previousState() {
    window.history.back();
  }
}
