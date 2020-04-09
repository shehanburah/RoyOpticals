import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
// import * as moment from 'moment';
import { IPettyCashStock, PettyCashStock } from 'app/shared/model/petty-cash-stock.model';
import { PettyCashStockService } from './petty-cash-stock.service';

@Component({
  selector: 'jhi-petty-cash-stock-update',
  templateUrl: './petty-cash-stock-update.component.html'
})
export class PettyCashStockUpdateComponent implements OnInit {
  isSaving: boolean;
  stockInDateDp: any;
  estNextStockInDateDp: any;

  editForm = this.fb.group({
    id: [],
    itemName: [],
    brandName: [],
    modelNumber: [],
    type: [],
    size: [],
    remarks: [],
    quantity: [],
    price: [],
    discountedPrice: [],
    stockInDate: [],
    estNextStockInDate: []
  });

  constructor(protected pettyCashStockService: PettyCashStockService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ pettyCashStock }) => {
      this.updateForm(pettyCashStock);
    });
  }

  updateForm(pettyCashStock: IPettyCashStock) {
    this.editForm.patchValue({
      id: pettyCashStock.id,
      itemName: pettyCashStock.itemName,
      brandName: pettyCashStock.brandName,
      modelNumber: pettyCashStock.modelNumber,
      type: pettyCashStock.type,
      size: pettyCashStock.size,
      remarks: pettyCashStock.remarks,
      quantity: pettyCashStock.quantity,
      price: pettyCashStock.price,
      discountedPrice: pettyCashStock.discountedPrice,
      stockInDate: pettyCashStock.stockInDate,
      estNextStockInDate: pettyCashStock.estNextStockInDate
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const pettyCashStock = this.createFromForm();
    if (pettyCashStock.id !== undefined) {
      this.subscribeToSaveResponse(this.pettyCashStockService.update(pettyCashStock));
    } else {
      this.subscribeToSaveResponse(this.pettyCashStockService.create(pettyCashStock));
    }
  }

  private createFromForm(): IPettyCashStock {
    return {
      ...new PettyCashStock(),
      id: this.editForm.get(['id']).value,
      itemName: this.editForm.get(['itemName']).value,
      brandName: this.editForm.get(['brandName']).value,
      modelNumber: this.editForm.get(['modelNumber']).value,
      type: this.editForm.get(['type']).value,
      size: this.editForm.get(['size']).value,
      remarks: this.editForm.get(['remarks']).value,
      quantity: this.editForm.get(['quantity']).value,
      price: this.editForm.get(['price']).value,
      discountedPrice: this.editForm.get(['discountedPrice']).value,
      stockInDate: this.editForm.get(['stockInDate']).value,
      estNextStockInDate: this.editForm.get(['estNextStockInDate']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPettyCashStock>>) {
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
