import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { RoyOpticalsTestModule } from '../../../test.module';
import { DescriptionDeleteDialogComponent } from 'app/entities/description/description-delete-dialog.component';
import { DescriptionService } from 'app/entities/description/description.service';

describe('Component Tests', () => {
  describe('Description Management Delete Component', () => {
    let comp: DescriptionDeleteDialogComponent;
    let fixture: ComponentFixture<DescriptionDeleteDialogComponent>;
    let service: DescriptionService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RoyOpticalsTestModule],
        declarations: [DescriptionDeleteDialogComponent]
      })
        .overrideTemplate(DescriptionDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DescriptionDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DescriptionService);
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
