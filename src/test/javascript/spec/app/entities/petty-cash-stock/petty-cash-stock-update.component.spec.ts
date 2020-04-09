import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { RoyOpticalsTestModule } from '../../../test.module';
import { PettyCashStockUpdateComponent } from 'app/entities/petty-cash-stock/petty-cash-stock-update.component';
import { PettyCashStockService } from 'app/entities/petty-cash-stock/petty-cash-stock.service';
import { PettyCashStock } from 'app/shared/model/petty-cash-stock.model';

describe('Component Tests', () => {
  describe('PettyCashStock Management Update Component', () => {
    let comp: PettyCashStockUpdateComponent;
    let fixture: ComponentFixture<PettyCashStockUpdateComponent>;
    let service: PettyCashStockService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RoyOpticalsTestModule],
        declarations: [PettyCashStockUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(PettyCashStockUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PettyCashStockUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PettyCashStockService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PettyCashStock('123');
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
        const entity = new PettyCashStock();
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
