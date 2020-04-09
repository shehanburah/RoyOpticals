import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RoyOpticalsTestModule } from '../../../test.module';
import { DescriptionDetailComponent } from 'app/entities/description/description-detail.component';
import { Description } from 'app/shared/model/description.model';

describe('Component Tests', () => {
  describe('Description Management Detail Component', () => {
    let comp: DescriptionDetailComponent;
    let fixture: ComponentFixture<DescriptionDetailComponent>;
    const route = ({ data: of({ description: new Description('123') }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RoyOpticalsTestModule],
        declarations: [DescriptionDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(DescriptionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DescriptionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.description).toEqual(jasmine.objectContaining({ id: '123' }));
      });
    });
  });
});
