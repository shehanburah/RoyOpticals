import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RoyOpticalsTestModule } from '../../../test.module';
import { MonthlyLogDetailComponent } from 'app/entities/monthly-log/monthly-log-detail.component';
import { MonthlyLog } from 'app/shared/model/monthly-log.model';

describe('Component Tests', () => {
  describe('MonthlyLog Management Detail Component', () => {
    let comp: MonthlyLogDetailComponent;
    let fixture: ComponentFixture<MonthlyLogDetailComponent>;
    const route = ({ data: of({ monthlyLog: new MonthlyLog('123') }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RoyOpticalsTestModule],
        declarations: [MonthlyLogDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(MonthlyLogDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MonthlyLogDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.monthlyLog).toEqual(jasmine.objectContaining({ id: '123' }));
      });
    });
  });
});
