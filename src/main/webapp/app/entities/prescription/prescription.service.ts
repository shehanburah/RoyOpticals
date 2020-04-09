import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPrescription } from 'app/shared/model/prescription.model';

type EntityResponseType = HttpResponse<IPrescription>;
type EntityArrayResponseType = HttpResponse<IPrescription[]>;

@Injectable({ providedIn: 'root' })
export class PrescriptionService {
  public resourceUrl = SERVER_API_URL + 'api/prescriptions';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/prescriptions';

  constructor(protected http: HttpClient) {}

  create(prescription: IPrescription): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prescription);
    return this.http
      .post<IPrescription>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(prescription: IPrescription): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prescription);
    return this.http
      .put<IPrescription>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<IPrescription>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPrescription[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPrescription[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(prescription: IPrescription): IPrescription {
    const copy: IPrescription = Object.assign({}, prescription, {
      date: prescription.date != null && prescription.date.isValid() ? prescription.date.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date != null ? moment(res.body.date) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((prescription: IPrescription) => {
        prescription.date = prescription.date != null ? moment(prescription.date) : null;
      });
    }
    return res;
  }
}
