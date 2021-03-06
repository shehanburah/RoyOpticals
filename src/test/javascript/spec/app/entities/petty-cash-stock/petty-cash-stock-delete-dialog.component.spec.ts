import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { RoyOpticalsTestModule } from '../../../test.module';
import { PettyCashStockDeleteDialogComponent } from 'app/entities/petty-cash-stock/petty-cash-stock-delete-dialog.component';
import { PettyCashStockService } from 'app/entities/petty-cash-stock/petty-cash-stock.service';

describe('Component Tests', () => {
  describe('PettyCashStock Management Delete Component', () => {
    let comp: PettyCashStockDeleteDialogComponent;
    let fixture: ComponentFixture<PettyCashStockDeleteDialogComponent>;
    let service: PettyCashStockService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RoyOpticalsTestModule],
        declarations: [PettyCashStockDeleteDialogComponent]
      })
        .overrideTemplate(PettyCashStockDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PettyCashStockDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PettyCashStockService);
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
