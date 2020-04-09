import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { RoyOpticalsTestModule } from '../../../test.module';
import { DailyLogDeleteDialogComponent } from 'app/entities/daily-log/daily-log-delete-dialog.component';
import { DailyLogService } from 'app/entities/daily-log/daily-log.service';

describe('Component Tests', () => {
  describe('DailyLog Management Delete Component', () => {
    let comp: DailyLogDeleteDialogComponent;
    let fixture: ComponentFixture<DailyLogDeleteDialogComponent>;
    let service: DailyLogService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RoyOpticalsTestModule],
        declarations: [DailyLogDeleteDialogComponent]
      })
        .overrideTemplate(DailyLogDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DailyLogDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DailyLogService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete('123');
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith('123');
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
