import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { RoyOpticalsTestModule } from '../../../test.module';
import { SolderingJobInvoiceUpdateComponent } from 'app/entities/soldering-job-invoice/soldering-job-invoice-update.component';
import { SolderingJobInvoiceService } from 'app/entities/soldering-job-invoice/soldering-job-invoice.service';
import { SolderingJobInvoice } from 'app/shared/model/soldering-job-invoice.model';

describe('Component Tests', () => {
  describe('SolderingJobInvoice Management Update Component', () => {
    let comp: SolderingJobInvoiceUpdateComponent;
    let fixture: ComponentFixture<SolderingJobInvoiceUpdateComponent>;
    let service: SolderingJobInvoiceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RoyOpticalsTestModule],
        declarations: [SolderingJobInvoiceUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(SolderingJobInvoiceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SolderingJobInvoiceUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SolderingJobInvoiceService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new SolderingJobInvoice('123');
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new SolderingJobInvoice();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
