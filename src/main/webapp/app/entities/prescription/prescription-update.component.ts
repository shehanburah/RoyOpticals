import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { IPrescription, Prescription } from 'app/shared/model/prescription.model';
import { PrescriptionService } from './prescription.service';

@Component({
  selector: 'jhi-prescription-update',
  templateUrl: './prescription-update.component.html'
})
export class PrescriptionUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    customerId: [],
    invoiceId: [],
    name: [],
    date: [],
    description: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected jhiAlertService: JhiAlertService,
    protected prescriptionService: PrescriptionService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ prescription }) => {
      this.updateForm(prescription);
    });

    this.activatedRoute.params.subscribe(params => { 
      if(  typeof params['customerId'] !== 'undefined' )
      this.editForm.patchValue({
        customerId:params['customerId'],
        invoiceId:params['invoiceId'],
        name:params['clientName']
      }) ;
    // In a real app: dispatch action to load the details here.
 });
  }

  updateForm(prescription: IPrescription) {
    this.editForm.patchValue({
      id: prescription.id,
      customerId: prescription.customerId,
      invoiceId: prescription.invoiceId,
      name: prescription.name,
      date: prescription.date != null ? prescription.date.format(DATE_TIME_FORMAT) : null,
      description: prescription.description
    });
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }

  setFileData(event, field: string, isImage) {
    return new Promise((resolve, reject) => {
      if (event && event.target && event.target.files && event.target.files[0]) {
        const file: File = event.target.files[0];
        if (isImage && !file.type.startsWith('image/')) {
          reject(`File was expected to be an image but was found to be ${file.type}`);
        } else {
          const filedContentType: string = field + 'ContentType';
          this.dataUtils.toBase64(file, base64Data => {
            this.editForm.patchValue({
              [field]: base64Data,
              [filedContentType]: file.type
            });
          });
        }
      } else {
        reject(`Base64 data was not set as file could not be extracted from passed parameter: ${event}`);
      }
    }).then(
      // eslint-disable-next-line no-console
      () => console.log('blob added'), // success
      this.onError
    );
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const prescription = this.createFromForm();
    if (prescription.id !== undefined) {
      this.subscribeToSaveResponse(this.prescriptionService.update(prescription));
    } else {
      this.subscribeToSaveResponse(this.prescriptionService.create(prescription));
    }
  }

  private createFromForm(): IPrescription {
    return {
      ...new Prescription(),
      id: this.editForm.get(['id']).value,
      customerId: this.editForm.get(['customerId']).value,
      invoiceId: this.editForm.get(['invoiceId']).value,
      name: this.editForm.get(['name']).value,
      date: this.editForm.get(['date']).value != null ? moment(this.editForm.get(['date']).value, DATE_TIME_FORMAT) : undefined,
      description: this.editForm.get(['description']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPrescription>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
