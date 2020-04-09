import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RoyOpticalsTestModule } from '../../../test.module';
import { DailyLogDetailComponent } from 'app/entities/daily-log/daily-log-detail.component';
import { DailyLog } from 'app/shared/model/daily-log.model';

describe('Component Tests', () => {
  describe('DailyLog Management Detail Component', () => {
    let comp: DailyLogDetailComponent;
    let fixture: ComponentFixture<DailyLogDetailComponent>;
    const route = ({ data: of({ dailyLog: new DailyLog('123') }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RoyOpticalsTestModule],
        declarations: [DailyLogDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(DailyLogDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DailyLogDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.dailyLog).toEqual(jasmine.objectContaining({ id: '123' }));
      });
    });
  });
});
