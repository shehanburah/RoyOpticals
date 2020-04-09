import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { RoyOpticalsTestModule } from '../../../test.module';
import { PrescriptionDeleteDialogComponent } from 'app/entities/prescription/prescription-delete-dialog.component';
import { PrescriptionService } from 'app/entities/prescription/prescription.service';

describe('Component Tests', () => {
  describe('Prescription Management Delete Component', () => {
    let comp: PrescriptionDeleteDialogComponent;
    let fixture: ComponentFixture<PrescriptionDeleteDialogComponent>;
    let service: PrescriptionService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RoyOpticalsTestModule],
        declarations: [PrescriptionDeleteDialogComponent]
      })
        .overrideTemplate(PrescriptionDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PrescriptionDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PrescriptionService);
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
