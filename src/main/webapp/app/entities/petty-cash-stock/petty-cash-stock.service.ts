import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPettyCashStock } from 'app/shared/model/petty-cash-stock.model';

type EntityResponseType = HttpResponse<IPettyCashStock>;
type EntityArrayResponseType = HttpResponse<IPettyCashStock[]>;

@Injectable({ providedIn: 'root' })
export class PettyCashStockService {
  public resourceUrl = SERVER_API_URL + 'api/petty-cash-stocks';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/petty-cash-stocks';

  constructor(protected http: HttpClient) {}

  create(pettyCashStock: IPettyCashStock): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pettyCashStock);
    return this.http
      .post<IPettyCashStock>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(pettyCashStock: IPettyCashStock): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pettyCashStock);
    return this.http
      .put<IPettyCashStock>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<IPettyCashStock>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPettyCashStock[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPettyCashStock[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(pettyCashStock: IPettyCashStock): IPettyCashStock {
    const copy: IPettyCashStock = Object.assign({}, pettyCashStock, {
      stockInDate:
        pettyCashStock.stockInDate != null && pettyCashStock.stockInDate.isValid() ? pettyCashStock.stockInDate.format(DATE_FORMAT) : null,
      estNextStockInDate:
        pettyCashStock.estNextStockInDate != null && pettyCashStock.estNextStockInDate.isValid()
          ? pettyCashStock.estNextStockInDate.format(DATE_FORMAT)
          : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.stockInDate = res.body.stockInDate != null ? moment(res.body.stockInDate) : null;
      res.body.estNextStockInDate = res.body.estNextStockInDate != null ? moment(res.body.estNextStockInDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((pettyCashStock: IPettyCashStock) => {
        pettyCashStock.stockInDate = pettyCashStock.stockInDate != null ? moment(pettyCashStock.stockInDate) : null;
        pettyCashStock.estNextStockInDate = pettyCashStock.estNextStockInDate != null ? moment(pettyCashStock.estNextStockInDate) : null;
      });
    }
    return res;
  }
}
