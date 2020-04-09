import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDescription } from 'app/shared/model/description.model';
import { DescriptionService } from './description.service';

@Component({
  selector: 'jhi-description-delete-dialog',
  templateUrl: './description-delete-dialog.component.html'
})
export class DescriptionDeleteDialogComponent {
  description: IDescription;

  constructor(
    protected descriptionService: DescriptionService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: string) {
    this.descriptionService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'descriptionListModification',
        content: 'Deleted an description'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-description-delete-popup',
  template: ''
})
export class DescriptionDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ description }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(DescriptionDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.description = description;
        this.ngbModalRef.result.then(
          () => {
            this.router.navigate(['/description', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          () => {
            this.router.navigate(['/description', { outlets: { popup: null } }]);
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
