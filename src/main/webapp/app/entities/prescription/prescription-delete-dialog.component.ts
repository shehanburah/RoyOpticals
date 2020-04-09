import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPrescription } from 'app/shared/model/prescription.model';
import { PrescriptionService } from './prescription.service';

@Component({
  selector: 'jhi-prescription-delete-dialog',
  templateUrl: './prescription-delete-dialog.component.html'
})
export class PrescriptionDeleteDialogComponent {
  prescription: IPrescription;

  constructor(
    protected prescriptionService: PrescriptionService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: string) {
    this.prescriptionService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'prescriptionListModification',
        content: 'Deleted an prescription'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-prescription-delete-popup',
  template: ''
})
export class PrescriptionDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ prescription }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(PrescriptionDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.prescription = prescription;
        this.ngbModalRef.result.then(
          () => {
            this.router.navigate(['/prescription', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          () => {
            this.router.navigate(['/prescription', { outlets: { popup: null } }]);
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
