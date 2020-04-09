import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMonthlyLog } from 'app/shared/model/monthly-log.model';
import { MonthlyLogService } from './monthly-log.service';

@Component({
  selector: 'jhi-monthly-log-delete-dialog',
  templateUrl: './monthly-log-delete-dialog.component.html'
})
export class MonthlyLogDeleteDialogComponent {
  monthlyLog: IMonthlyLog;

  constructor(
    protected monthlyLogService: MonthlyLogService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: string) {
    this.monthlyLogService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'monthlyLogListModification',
        content: 'Deleted an monthlyLog'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-monthly-log-delete-popup',
  template: ''
})
export class MonthlyLogDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ monthlyLog }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(MonthlyLogDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.monthlyLog = monthlyLog;
        this.ngbModalRef.result.then(
          () => {
            this.router.navigate(['/monthly-log', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          () => {
            this.router.navigate(['/monthly-log', { outlets: { popup: null } }]);
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
