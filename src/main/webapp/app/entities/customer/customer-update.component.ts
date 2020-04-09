import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ICustomer, Customer } from 'app/shared/model/customer.model';
import { CustomerService } from './customer.service';

@Component({
  selector: 'jhi-customer-update',
  templateUrl: './customer-update.component.html'
})
export class CustomerUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    title: [],
    firstName: [],
    lastName: [],
    dob: [],
    city: [],
    address: [],
    mobile: [],
    landline: [],
    gender: [],
    registered: []
  });

  constructor(protected customerService: CustomerService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ customer }) => {
      this.updateForm(customer);
    });
  }

  updateForm(customer: ICustomer) {
    this.editForm.patchValue({
      id: customer.id,
      title: customer.title,
      firstName: customer.firstName,
      lastName: customer.lastName,
      dob: customer.dob != null ? customer.dob.format(DATE_TIME_FORMAT) : null,
      city: customer.city,
      address: customer.address,
      mobile: customer.mobile,
      landline: customer.landline,
      gender: customer.gender,
      registered: customer.registered != null ? customer.registered.format(DATE_TIME_FORMAT) : null
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const customer = this.createFromForm();
    if (customer.id !== undefined) {
      this.subscribeToSaveResponse(this.customerService.update(customer));
    } else {
      this.subscribeToSaveResponse(this.customerService.create(customer));
    }
  }

  private createFromForm(): ICustomer {
    return {
      ...new Customer(),
      id: this.editForm.get(['id']).value,
      title: this.editForm.get(['title']).value,
      firstName: this.editForm.get(['firstName']).value,
      lastName: this.editForm.get(['lastName']).value,
      dob: this.editForm.get(['dob']).value != null ? moment(this.editForm.get(['dob']).value, DATE_TIME_FORMAT) : undefined,
      city: this.editForm.get(['city']).value,
      address: this.editForm.get(['address']).value,
      mobile: this.editForm.get(['mobile']).value,
      landline: this.editForm.get(['landline']).value,
      gender: this.editForm.get(['gender']).value,
      registered:
        this.editForm.get(['registered']).value != null ? moment(this.editForm.get(['registered']).value, DATE_TIME_FORMAT) : undefined
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomer>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }

  nowDateTime(){
    this.editForm.patchValue(
      {
        registered:moment().format('YYYY-MM-DDThh:MM')
      }
    )
  }
}
