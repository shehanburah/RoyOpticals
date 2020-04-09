import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPettyCashStock } from 'app/shared/model/petty-cash-stock.model';
import { PettyCashStockService } from './petty-cash-stock.service';

@Component({
  selector: 'jhi-petty-cash-stock-delete-dialog',
  templateUrl: './petty-cash-stock-delete-dialog.component.html'
})
export class PettyCashStockDeleteDialogComponent {
  pettyCashStock: IPettyCashStock;

  constructor(
    protected pettyCashStockService: PettyCashStockService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: string) {
    this.pettyCashStockService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'pettyCashStockListModification',
        content: 'Deleted an pettyCashStock'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-petty-cash-stock-delete-popup',
  template: ''
})
export class PettyCashStockDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ pettyCashStock }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(PettyCashStockDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.pettyCashStock = pettyCashStock;
        this.ngbModalRef.result.then(
          () => {
            this.router.navigate(['/petty-cash-stock', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          () => {
            this.router.navigate(['/petty-cash-stock', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
