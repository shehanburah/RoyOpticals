import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { IDailyLog, DailyLog } from 'app/shared/model/daily-log.model';
import { DailyLogService } from './daily-log.service';

@Component({
  selector: 'jhi-daily-log-update',
  templateUrl: './daily-log-update.component.html'
})
export class DailyLogUpdateComponent implements OnInit {
  isSaving: boolean;
  createdDateDp: any;

  editForm = this.fb.group({
    id: [],
    createdDate: [],
    rx: [],
    invoiceId: [],
    solderingJobInvoiceId: [],
    customer: [],
    customerId: [],
    description: [],
    amount: []
  });

  constructor(protected dailyLogService: DailyLogService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ dailyLog }) => {
      this.updateForm(dailyLog);
    });
  }

  updateForm(dailyLog: IDailyLog) {
    this.editForm.patchValue({
      id: dailyLog.id,
      createdDate: dailyLog.createdDate,
      rx: dailyLog.rx,
      invoiceId: dailyLog.invoiceId,
      solderingJobInvoiceId: dailyLog.solderingJobInvoiceId,
      customer: dailyLog.customer,
      customerId: dailyLog.customerId,
      description: dailyLog.description,
      amount: dailyLog.amount
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const dailyLog = this.createFromForm();
    if (dailyLog.id !== undefined) {
      this.subscribeToSaveResponse(this.dailyLogService.update(dailyLog));
    } else {
      this.subscribeToSaveResponse(this.dailyLogService.create(dailyLog));
    }
  }

  private createFromForm(): IDailyLog {
    return {
      ...new DailyLog(),
      id: this.editForm.get(['id']).value,
      createdDate: this.editForm.get(['createdDate']).value,
      rx: this.editForm.get(['rx']).value,
      invoiceId: this.editForm.get(['invoiceId']).value,
      solderingJobInvoiceId: this.editForm.get(['solderingJobInvoiceId']).value,
      customer: this.editForm.get(['customer']).value,
      customerId: this.editForm.get(['customerId']).value,
      description: this.editForm.get(['description']).value,
      amount: this.editForm.get(['amount']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDailyLog>>) {
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
