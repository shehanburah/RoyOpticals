import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SolderingJobInvoiceService } from 'app/entities/soldering-job-invoice/soldering-job-invoice.service';
import { ISolderingJobInvoice, SolderingJobInvoice } from 'app/shared/model/soldering-job-invoice.model';

describe('Service Tests', () => {
  describe('SolderingJobInvoice Service', () => {
    let injector: TestBed;
    let service: SolderingJobInvoiceService;
    let httpMock: HttpTestingController;
    let elemDefault: ISolderingJobInvoice;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(SolderingJobInvoiceService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new SolderingJobInvoice('ID', 'AAAAAAA', 0, currentDate, false, currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            orderDate: currentDate.format(DATE_FORMAT),
            deliveryDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        service
          .find('123')
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a SolderingJobInvoice', () => {
        const returnedFromService = Object.assign(
          {
            id: 'ID',
            orderDate: currentDate.format(DATE_FORMAT),
            deliveryDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            orderDate: currentDate,
            deliveryDate: currentDate
          },
          returnedFromService
        );
        service
          .create(new SolderingJobInvoice(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a SolderingJobInvoice', () => {
        const returnedFromService = Object.assign(
          {
            customerId: 'BBBBBB',
            price: 1,
            orderDate: currentDate.format(DATE_FORMAT),
            delivered: true,
            deliveryDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            orderDate: currentDate,
            deliveryDate: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of SolderingJobInvoice', () => {
        const returnedFromService = Object.assign(
          {
            customerId: 'BBBBBB',
            price: 1,
            orderDate: currentDate.format(DATE_FORMAT),
            delivered: true,
            deliveryDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            orderDate: currentDate,
            deliveryDate: currentDate
          },
          returnedFromService
        );
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a SolderingJobInvoice', () => {
        service.delete('123').subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
