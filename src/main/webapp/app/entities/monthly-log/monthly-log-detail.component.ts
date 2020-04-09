import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMonthlyLog } from 'app/shared/model/monthly-log.model';

@Component({
  selector: 'jhi-monthly-log-detail',
  templateUrl: './monthly-log-detail.component.html'
})
export class MonthlyLogDetailComponent implements OnInit {
  monthlyLog: IMonthlyLog;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ monthlyLog }) => {
      this.monthlyLog = monthlyLog;
    });
  }

  previousState() {
    window.history.back();
  }
}
