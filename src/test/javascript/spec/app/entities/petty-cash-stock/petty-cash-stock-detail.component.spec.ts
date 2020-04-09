import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RoyOpticalsTestModule } from '../../../test.module';
import { PettyCashStockDetailComponent } from 'app/entities/petty-cash-stock/petty-cash-stock-detail.component';
import { PettyCashStock } from 'app/shared/model/petty-cash-stock.model';

describe('Component Tests', () => {
  describe('PettyCashStock Management Detail Component', () => {
    let comp: PettyCashStockDetailComponent;
    let fixture: ComponentFixture<PettyCashStockDetailComponent>;
    const route = ({ data: of({ pettyCashStock: new PettyCashStock('123') }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RoyOpticalsTestModule],
        declarations: [PettyCashStockDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PettyCashStockDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PettyCashStockDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.pettyCashStock).toEqual(jasmine.objectContaining({ id: '123' }));
      });
    });
  });
});
