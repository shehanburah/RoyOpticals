import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISolderingJobInvoice } from 'app/shared/model/soldering-job-invoice.model';
import { SolderingJobInvoiceService } from './soldering-job-invoice.service';

@Component({
  selector: 'jhi-soldering-job-invoice-delete-dialog',
  templateUrl: './soldering-job-invoice-delete-dialog.component.html'
})
export class SolderingJobInvoiceDeleteDialogComponent {
  solderingJobInvoice: ISolderingJobInvoice;

  constructor(
    protected solderingJobInvoiceService: SolderingJobInvoiceService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: string) {
    this.solderingJobInvoiceService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'solderingJobInvoiceListModification',
        content: 'Deleted an solderingJobInvoice'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-soldering-job-invoice-delete-popup',
  template: ''
})
export class SolderingJobInvoiceDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ solderingJobInvoice }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(SolderingJobInvoiceDeleteDialogComponent as Component, {
          size: 'lg',
          backdrop: 'static'
        });
        this.ngbModalRef.componentInstance.solderingJobInvoice = solderingJobInvoice;
        this.ngbModalRef.result.then(
          () => {
            this.router.navigate(['/soldering-job-invoice', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          () => {
            this.router.navigate(['/soldering-job-invoice', { outlets: { popup: null } }]);
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
