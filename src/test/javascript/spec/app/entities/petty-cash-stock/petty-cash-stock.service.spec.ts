import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { PettyCashStockService } from 'app/entities/petty-cash-stock/petty-cash-stock.service';
import { IPettyCashStock, PettyCashStock } from 'app/shared/model/petty-cash-stock.model';

describe('Service Tests', () => {
  describe('PettyCashStock Service', () => {
    let injector: TestBed;
    let service: PettyCashStockService;
    let httpMock: HttpTestingController;
    let elemDefault: IPettyCashStock;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(PettyCashStockService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new PettyCashStock('ID', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 0, 'AAAAAAA', 0, 0, 0, currentDate, currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            stockInDate: currentDate.format(DATE_FORMAT),
            estNextStockInDate: currentDate.format(DATE_FORMAT)
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

      it('should create a PettyCashStock', () => {
        const returnedFromService = Object.assign(
          {
            id: 'ID',
            stockInDate: currentDate.format(DATE_FORMAT),
            estNextStockInDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            stockInDate: currentDate,
            estNextStockInDate: currentDate
          },
          returnedFromService
        );
        service
          .create(new PettyCashStock(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a PettyCashStock', () => {
        const returnedFromService = Object.assign(
          {
            itemName: 'BBBBBB',
            brandName: 'BBBBBB',
            modelNumber: 'BBBBBB',
            type: 'BBBBBB',
            size: 1,
            remarks: 'BBBBBB',
            quantity: 1,
            price: 1,
            discountedPrice: 1,
            stockInDate: currentDate.format(DATE_FORMAT),
            estNextStockInDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            stockInDate: currentDate,
            estNextStockInDate: currentDate
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

      it('should return a list of PettyCashStock', () => {
        const returnedFromService = Object.assign(
          {
            itemName: 'BBBBBB',
            brandName: 'BBBBBB',
            modelNumber: 'BBBBBB',
            type: 'BBBBBB',
            size: 1,
            remarks: 'BBBBBB',
            quantity: 1,
            price: 1,
            discountedPrice: 1,
            stockInDate: currentDate.format(DATE_FORMAT),
            estNextStockInDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            stockInDate: currentDate,
            estNextStockInDate: currentDate
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

      it('should delete a PettyCashStock', () => {
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
