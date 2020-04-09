import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IMonthlyLog } from 'app/shared/model/monthly-log.model';

type EntityResponseType = HttpResponse<IMonthlyLog>;
type EntityArrayResponseType = HttpResponse<IMonthlyLog[]>;

@Injectable({ providedIn: 'root' })
export class MonthlyLogService {
  public resourceUrl = SERVER_API_URL + 'api/monthly-logs';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/monthly-logs';

  constructor(protected http: HttpClient) {}

  create(monthlyLog: IMonthlyLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(monthlyLog);
    return this.http
      .post<IMonthlyLog>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(monthlyLog: IMonthlyLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(monthlyLog);
    return this.http
      .put<IMonthlyLog>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<IMonthlyLog>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMonthlyLog[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMonthlyLog[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(monthlyLog: IMonthlyLog): IMonthlyLog {
    const copy: IMonthlyLog = Object.assign({}, monthlyLog, {
      createdDate: monthlyLog.createdDate != null && monthlyLog.createdDate.isValid() ? monthlyLog.createdDate.format(DATE_FORMAT) : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdDate = res.body.createdDate != null ? moment(res.body.createdDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((monthlyLog: IMonthlyLog) => {
        monthlyLog.createdDate = monthlyLog.createdDate != null ? moment(monthlyLog.createdDate) : null;
      });
    }
    return res;
  }
}
