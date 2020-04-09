import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { IMonthlyLog, MonthlyLog } from 'app/shared/model/monthly-log.model';
import { MonthlyLogService } from './monthly-log.service';

@Component({
  selector: 'jhi-monthly-log-update',
  templateUrl: './monthly-log-update.component.html'
})
export class MonthlyLogUpdateComponent implements OnInit {
  isSaving: boolean;
  createdDateDp: any;

  editForm = this.fb.group({
    id: [],
    identification: [],
    reportObtainedBy: [],
    createdDate: [],
    amount: [],
    remark: []
  });

  constructor(protected monthlyLogService: MonthlyLogService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ monthlyLog }) => {
      this.updateForm(monthlyLog);
    });
  }

  updateForm(monthlyLog: IMonthlyLog) {
    this.editForm.patchValue({
      id: monthlyLog.id,
      identification: monthlyLog.identification,
      reportObtainedBy: monthlyLog.reportObtainedBy,
      createdDate: monthlyLog.createdDate,
      amount: monthlyLog.amount,
      remark: monthlyLog.remark
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const monthlyLog = this.createFromForm();
    if (monthlyLog.id !== undefined) {
      this.subscribeToSaveResponse(this.monthlyLogService.update(monthlyLog));
    } else {
      this.subscribeToSaveResponse(this.monthlyLogService.create(monthlyLog));
    }
  }

  private createFromForm(): IMonthlyLog {
    return {
      ...new MonthlyLog(),
      id: this.editForm.get(['id']).value,
      identification: this.editForm.get(['identification']).value,
      reportObtainedBy: this.editForm.get(['reportObtainedBy']).value,
      createdDate: this.editForm.get(['createdDate']).value,
      amount: this.editForm.get(['amount']).value,
      remark: this.editForm.get(['remark']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMonthlyLog>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
