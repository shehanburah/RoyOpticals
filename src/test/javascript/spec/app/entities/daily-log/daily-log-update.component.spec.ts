import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { RoyOpticalsTestModule } from '../../../test.module';
import { DailyLogUpdateComponent } from 'app/entities/daily-log/daily-log-update.component';
import { DailyLogService } from 'app/entities/daily-log/daily-log.service';
import { DailyLog } from 'app/shared/model/daily-log.model';

describe('Component Tests', () => {
  describe('DailyLog Management Update Component', () => {
    let comp: DailyLogUpdateComponent;
    let fixture: ComponentFixture<DailyLogUpdateComponent>;
    let service: DailyLogService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RoyOpticalsTestModule],
        declarations: [DailyLogUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(DailyLogUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DailyLogUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DailyLogService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new DailyLog('123');
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
        const entity = new DailyLog();
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
