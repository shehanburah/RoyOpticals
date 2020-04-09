import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDailyLog } from 'app/shared/model/daily-log.model';

@Component({
  selector: 'jhi-daily-log-detail',
  templateUrl: './daily-log-detail.component.html'
})
export class DailyLogDetailComponent implements OnInit {
  dailyLog: IDailyLog;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ dailyLog }) => {
      this.dailyLog = dailyLog;
    });
  }

  previousState() {
    window.history.back();
  }
}
