import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IItems, Items } from 'app/shared/model/items.model';
import { ItemsService } from './items.service';

@Component({
  selector: 'jhi-items-update',
  templateUrl: './items-update.component.html'
})
export class ItemsUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    invoiceId: [],
    description: [],
    quantity: [],
    rate: [],
    amount: []
  });

  constructor(protected itemsService: ItemsService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ items }) => {
      this.updateForm(items);
    });
  }

  updateForm(items: IItems) {
    this.editForm.patchValue({
      id: items.id,
      invoiceId: items.invoiceId,
      description: items.description,
      quantity: items.quantity,
      rate: items.rate,
      amount: items.amount
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const items = this.createFromForm();
    if (items.id !== undefined) {
      this.subscribeToSaveResponse(this.itemsService.update(items));
    } else {
      this.subscribeToSaveResponse(this.itemsService.create(items));
    }
  }

  private createFromForm(): IItems {
    return {
      ...new Items(),
      id: this.editForm.get(['id']).value,
      invoiceId: this.editForm.get(['invoiceId']).value,
      description: this.editForm.get(['description']).value,
      quantity: this.editForm.get(['quantity']).value,
      rate: this.editForm.get(['rate']).value,
      amount: this.editForm.get(['amount']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IItems>>) {
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
