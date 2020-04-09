import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IDailyLog } from 'app/shared/model/daily-log.model';

type EntityResponseType = HttpResponse<IDailyLog>;
type EntityArrayResponseType = HttpResponse<IDailyLog[]>;

@Injectable({ providedIn: 'root' })
export class DailyLogService {
  public resourceUrl = SERVER_API_URL + 'api/daily-logs';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/daily-logs';

  constructor(protected http: HttpClient) {}

  create(dailyLog: IDailyLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dailyLog);
    return this.http
      .post<IDailyLog>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(dailyLog: IDailyLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dailyLog);
    return this.http
      .put<IDailyLog>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<IDailyLog>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDailyLog[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDailyLog[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(dailyLog: IDailyLog): IDailyLog {
    const copy: IDailyLog = Object.assign({}, dailyLog, {
      createdDate: dailyLog.createdDate != null && dailyLog.createdDate.isValid() ? dailyLog.createdDate.format(DATE_FORMAT) : null
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
      res.body.forEach((dailyLog: IDailyLog) => {
        dailyLog.createdDate = dailyLog.createdDate != null ? moment(dailyLog.createdDate) : null;
      });
    }
    return res;
  }
}
