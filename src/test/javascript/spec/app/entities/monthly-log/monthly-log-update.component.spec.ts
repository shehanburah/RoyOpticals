import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { RoyOpticalsTestModule } from '../../../test.module';
import { MonthlyLogUpdateComponent } from 'app/entities/monthly-log/monthly-log-update.component';
import { MonthlyLogService } from 'app/entities/monthly-log/monthly-log.service';
import { MonthlyLog } from 'app/shared/model/monthly-log.model';

describe('Component Tests', () => {
  describe('MonthlyLog Management Update Component', () => {
    let comp: MonthlyLogUpdateComponent;
    let fixture: ComponentFixture<MonthlyLogUpdateComponent>;
    let service: MonthlyLogService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RoyOpticalsTestModule],
        declarations: [MonthlyLogUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(MonthlyLogUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MonthlyLogUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MonthlyLogService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new MonthlyLog('123');
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
        const entity = new MonthlyLog();
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
