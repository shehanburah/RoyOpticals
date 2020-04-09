import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDailyLog } from 'app/shared/model/daily-log.model';
import { DailyLogService } from './daily-log.service';

@Component({
  selector: 'jhi-daily-log-delete-dialog',
  templateUrl: './daily-log-delete-dialog.component.html'
})
export class DailyLogDeleteDialogComponent {
  dailyLog: IDailyLog;

  constructor(protected dailyLogService: DailyLogService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: string) {
    this.dailyLogService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'dailyLogListModification',
        content: 'Deleted an dailyLog'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-daily-log-delete-popup',
  template: ''
})
export class DailyLogDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ dailyLog }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(DailyLogDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.dailyLog = dailyLog;
        this.ngbModalRef.result.then(
          () => {
            this.router.navigate(['/daily-log', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          () => {
            this.router.navigate(['/daily-log', { outlets: { popup: null } }]);
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
