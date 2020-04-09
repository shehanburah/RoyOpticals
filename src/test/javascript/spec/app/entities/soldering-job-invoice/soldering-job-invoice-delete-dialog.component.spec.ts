import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { RoyOpticalsTestModule } from '../../../test.module';
import { SolderingJobInvoiceDeleteDialogComponent } from 'app/entities/soldering-job-invoice/soldering-job-invoice-delete-dialog.component';
import { SolderingJobInvoiceService } from 'app/entities/soldering-job-invoice/soldering-job-invoice.service';

describe('Component Tests', () => {
  describe('SolderingJobInvoice Management Delete Component', () => {
    let comp: SolderingJobInvoiceDeleteDialogComponent;
    let fixture: ComponentFixture<SolderingJobInvoiceDeleteDialogComponent>;
    let service: SolderingJobInvoiceService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RoyOpticalsTestModule],
        declarations: [SolderingJobInvoiceDeleteDialogComponent]
      })
        .overrideTemplate(SolderingJobInvoiceDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SolderingJobInvoiceDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SolderingJobInvoiceService);
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
