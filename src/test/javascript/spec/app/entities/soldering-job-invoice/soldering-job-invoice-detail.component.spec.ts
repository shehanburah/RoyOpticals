import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RoyOpticalsTestModule } from '../../../test.module';
import { SolderingJobInvoiceDetailComponent } from 'app/entities/soldering-job-invoice/soldering-job-invoice-detail.component';
import { SolderingJobInvoice } from 'app/shared/model/soldering-job-invoice.model';

describe('Component Tests', () => {
  describe('SolderingJobInvoice Management Detail Component', () => {
    let comp: SolderingJobInvoiceDetailComponent;
    let fixture: ComponentFixture<SolderingJobInvoiceDetailComponent>;
    const route = ({ data: of({ solderingJobInvoice: new SolderingJobInvoice('123') }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RoyOpticalsTestModule],
        declarations: [SolderingJobInvoiceDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(SolderingJobInvoiceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SolderingJobInvoiceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.solderingJobInvoice).toEqual(jasmine.objectContaining({ id: '123' }));
      });
    });
  });
});
