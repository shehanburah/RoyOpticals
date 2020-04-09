import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { IDescription, Description } from 'app/shared/model/description.model';
import { DescriptionService } from './description.service';
import { InvoiceService } from '../invoice/invoice.service';

@Component({
  selector: 'jhi-description-update',
  templateUrl: './description-update.component.html'
})
export class DescriptionUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    customerId: [],
    invoiceId: [],
    sphLeft: [],
    cylLeft: [],
    axisLeft: [],
    readingLeft: [],
    sphRight: [],
    cylRight: [],
    axisRight: [],
    readingRight: [],
    orderDate: [],
    deleveryDate: [],
    orderNo: [],
    frame: [],
    modelNo: [],
    size: [],
    colour: [],
    lType: [],
    lSize: [],
    pd: [],
    inset: [],
    name: [],
    remarks: []
  });

  reverse = false;
  constructor(protected descriptionService: DescriptionService,
    protected invoiceService: InvoiceService,
    protected activatedRoute: ActivatedRoute, private fb: FormBuilder) { }

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ description }) => {
      this.updateForm(description);

      // alert(JSON.stringify(description))

      if (description ) {
        this.activatedRoute.params.subscribe(params => {

          // alert( 'params');
          if (typeof params['customerId'] !== 'undefined') {
            this.editForm.patchValue({
              customerId: params['customerId'],
              invoiceId: params['invoiceId'],
              name: params['clientName']
            });

            this.invoiceService.find(params['invoiceId']).subscribe(res => {
              this.editForm.patchValue({
                orderNo: typeof Number(res.body['rx']) === 'undefined' ? 0 : Number(res.body["rx"])

              });
            })
          } else {
            // alert( 'not null')
          }

        });
      }
    });




    // this.descriptionService
    //   .search({
    //     page: 0,
    //     query: '*',
    //     size: 1,
    //     sort: this.sort()
    //   })
    //   .subscribe((res) => {
    //     // alert(JSON.stringify(res.body))
    //     if (res.body.length > 0) {
    //       this.editForm.patchValue({
    //         orderNo: typeof Number(res.body[0]['orderNo']) === 'undefined' ? 0 : Number(res.body[0].orderNo)

    //       });
    //     } else {
    //       this.editForm.patchValue({
    //         orderNo: 0

    //       });
    //     }

    //   });

  }

  updateForm(description: IDescription) {
    this.editForm.patchValue({
      id: description.id,
      customerId: description.customerId,
      invoiceId: description.invoiceId,
      sphLeft: description.sphLeft,
      cylLeft: description.cylLeft,
      axisLeft: description.axisLeft,
      readingLeft: description.readingLeft,
      sphRight: description.sphRight,
      cylRight: description.cylRight,
      axisRight: description.axisRight,
      readingRight: description.readingRight,
      orderDate: description.orderDate != null ? description.orderDate.format(DATE_TIME_FORMAT) : null,
      deleveryDate: description.deleveryDate != null ? description.deleveryDate.format(DATE_TIME_FORMAT) : null,
      orderNo: description.orderNo,
      frame: description.frame,
      modelNo: description.modelNo,
      size: description.size,
      colour: description.colour,
      lType: description.lType,
      lSize: description.lSize,
      pd: description.pd,
      inset: description.inset,
      name: description.name,
      remarks: description.remarks
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const description = this.createFromForm();
    if (description.id !== undefined) {
      this.subscribeToSaveResponse(this.descriptionService.update(description));
    } else {
      this.subscribeToSaveResponse(this.descriptionService.create(description));
    }
  }

  private createFromForm(): IDescription {
    return {
      ...new Description(),
      id: this.editForm.get(['id']).value,
      customerId: this.editForm.get(['customerId']).value,
      invoiceId: this.editForm.get(['invoiceId']).value,
      sphLeft: this.editForm.get(['sphLeft']).value,
      cylLeft: this.editForm.get(['cylLeft']).value,
      axisLeft: this.editForm.get(['axisLeft']).value,
      readingLeft: this.editForm.get(['readingLeft']).value,
      sphRight: this.editForm.get(['sphRight']).value,
      cylRight: this.editForm.get(['cylRight']).value,
      axisRight: this.editForm.get(['axisRight']).value,
      readingRight: this.editForm.get(['readingRight']).value,
      orderDate:
        this.editForm.get(['orderDate']).value != null ? moment(this.editForm.get(['orderDate']).value, DATE_TIME_FORMAT) : undefined,
      deleveryDate:
        this.editForm.get(['deleveryDate']).value != null ? moment(this.editForm.get(['deleveryDate']).value, DATE_TIME_FORMAT) : undefined,
      orderNo: this.editForm.get(['orderNo']).value,
      frame: this.editForm.get(['frame']).value,
      modelNo: this.editForm.get(['modelNo']).value,
      size: this.editForm.get(['size']).value,
      colour: this.editForm.get(['colour']).value,
      lType: this.editForm.get(['lType']).value,
      lSize: this.editForm.get(['lSize']).value,
      pd: this.editForm.get(['pd']).value,
      inset: this.editForm.get(['inset']).value,
      name: this.editForm.get(['name']).value,
      remarks: this.editForm.get(['remarks']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDescription>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }

  sort() {
    const result = ['id' + ',' + (this.reverse ? 'asc' : 'desc')];

    return result;
  }
}
