import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { InvoiceService } from 'app/entities/invoice/invoice.service';
import { IInvoice, Invoice } from 'app/shared/model/invoice.model';

describe('Service Tests', () => {
  describe('Invoice Service', () => {
    let injector: TestBed;
    let service: InvoiceService;
    let httpMock: HttpTestingController;
    let elemDefault: IInvoice;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(InvoiceService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Invoice('ID', 'AAAAAAA', 0, currentDate, currentDate, 0, 0, false, false);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            deleveryDate: currentDate.format(DATE_TIME_FORMAT),
            orderDate: currentDate.format(DATE_TIME_FORMAT)
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

      it('should create a Invoice', () => {
        const returnedFromService = Object.assign(
          {
            id: 'ID',
            deleveryDate: currentDate.format(DATE_TIME_FORMAT),
            orderDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            deleveryDate: currentDate,
            orderDate: currentDate
          },
          returnedFromService
        );
        service
          .create(new Invoice(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a Invoice', () => {
        const returnedFromService = Object.assign(
          {
            customerId: 'BBBBBB',
            rx: 1,
            deleveryDate: currentDate.format(DATE_TIME_FORMAT),
            orderDate: currentDate.format(DATE_TIME_FORMAT),
            advance: 1,
            balance: 1,
            delivered: true,
            paid: true
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            deleveryDate: currentDate,
            orderDate: currentDate
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

      it('should return a list of Invoice', () => {
        const returnedFromService = Object.assign(
          {
            customerId: 'BBBBBB',
            rx: 1,
            deleveryDate: currentDate.format(DATE_TIME_FORMAT),
            orderDate: currentDate.format(DATE_TIME_FORMAT),
            advance: 1,
            balance: 1,
            delivered: true,
            paid: true
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            deleveryDate: currentDate,
            orderDate: currentDate
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

      it('should delete a Invoice', () => {
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
