import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { DescriptionService } from 'app/entities/description/description.service';
import { IDescription, Description } from 'app/shared/model/description.model';

describe('Service Tests', () => {
  describe('Description Service', () => {
    let injector: TestBed;
    let service: DescriptionService;
    let httpMock: HttpTestingController;
    let elemDefault: IDescription;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(DescriptionService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Description(
        'ID',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        currentDate,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA'
      );
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            orderDate: currentDate.format(DATE_TIME_FORMAT),
            deleveryDate: currentDate.format(DATE_TIME_FORMAT)
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

      it('should create a Description', () => {
        const returnedFromService = Object.assign(
          {
            id: 'ID',
            orderDate: currentDate.format(DATE_TIME_FORMAT),
            deleveryDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            orderDate: currentDate,
            deleveryDate: currentDate
          },
          returnedFromService
        );
        service
          .create(new Description(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a Description', () => {
        const returnedFromService = Object.assign(
          {
            customerId: 'BBBBBB',
            invoiceId: 'BBBBBB',
            sphLeft: 'BBBBBB',
            cylLeft: 'BBBBBB',
            axisLeft: 'BBBBBB',
            readingLeft: 'BBBBBB',
            sphRight: 'BBBBBB',
            cylRight: 'BBBBBB',
            axisRight: 'BBBBBB',
            readingRight: 'BBBBBB',
            orderDate: currentDate.format(DATE_TIME_FORMAT),
            deleveryDate: currentDate.format(DATE_TIME_FORMAT),
            orderNo: 'BBBBBB',
            frame: 'BBBBBB',
            modelNo: 'BBBBBB',
            size: 'BBBBBB',
            colour: 'BBBBBB',
            lType: 'BBBBBB',
            lSize: 'BBBBBB',
            pd: 'BBBBBB',
            inset: 'BBBBBB',
            name: 'BBBBBB',
            remarks: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            orderDate: currentDate,
            deleveryDate: currentDate
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

      it('should return a list of Description', () => {
        const returnedFromService = Object.assign(
          {
            customerId: 'BBBBBB',
            invoiceId: 'BBBBBB',
            sphLeft: 'BBBBBB',
            cylLeft: 'BBBBBB',
            axisLeft: 'BBBBBB',
            readingLeft: 'BBBBBB',
            sphRight: 'BBBBBB',
            cylRight: 'BBBBBB',
            axisRight: 'BBBBBB',
            readingRight: 'BBBBBB',
            orderDate: currentDate.format(DATE_TIME_FORMAT),
            deleveryDate: currentDate.format(DATE_TIME_FORMAT),
            orderNo: 'BBBBBB',
            frame: 'BBBBBB',
            modelNo: 'BBBBBB',
            size: 'BBBBBB',
            colour: 'BBBBBB',
            lType: 'BBBBBB',
            lSize: 'BBBBBB',
            pd: 'BBBBBB',
            inset: 'BBBBBB',
            name: 'BBBBBB',
            remarks: 'BBBBBB'
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            orderDate: currentDate,
            deleveryDate: currentDate
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

      it('should delete a Description', () => {
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
