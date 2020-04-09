import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ISolderingJobInvoice } from 'app/shared/model/soldering-job-invoice.model';

type EntityResponseType = HttpResponse<ISolderingJobInvoice>;
type EntityArrayResponseType = HttpResponse<ISolderingJobInvoice[]>;

@Injectable({ providedIn: 'root' })
export class SolderingJobInvoiceService {
  public resourceUrl = SERVER_API_URL + 'api/soldering-job-invoices';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/soldering-job-invoices';

  constructor(protected http: HttpClient) {}

  create(solderingJobInvoice: ISolderingJobInvoice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(solderingJobInvoice);
    return this.http
      .post<ISolderingJobInvoice>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(solderingJobInvoice: ISolderingJobInvoice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(solderingJobInvoice);
    return this.http
      .put<ISolderingJobInvoice>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<ISolderingJobInvoice>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISolderingJobInvoice[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISolderingJobInvoice[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(solderingJobInvoice: ISolderingJobInvoice): ISolderingJobInvoice {
    const copy: ISolderingJobInvoice = Object.assign({}, solderingJobInvoice, {
      orderDate:
        solderingJobInvoice.orderDate != null && solderingJobInvoice.orderDate.isValid()
          ? solderingJobInvoice.orderDate.format(DATE_FORMAT)
          : null,
      deliveryDate:
        solderingJobInvoice.deliveryDate != null && solderingJobInvoice.deliveryDate.isValid()
          ? solderingJobInvoice.deliveryDate.format(DATE_FORMAT)
          : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.orderDate = res.body.orderDate != null ? moment(res.body.orderDate) : null;
      res.body.deliveryDate = res.body.deliveryDate != null ? moment(res.body.deliveryDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((solderingJobInvoice: ISolderingJobInvoice) => {
        solderingJobInvoice.orderDate = solderingJobInvoice.orderDate != null ? moment(solderingJobInvoice.orderDate) : null;
        solderingJobInvoice.deliveryDate = solderingJobInvoice.deliveryDate != null ? moment(solderingJobInvoice.deliveryDate) : null;
      });
    }
    return res;
  }
}
