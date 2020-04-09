import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IDescription } from 'app/shared/model/description.model';

type EntityResponseType = HttpResponse<IDescription>;
type EntityArrayResponseType = HttpResponse<IDescription[]>;

@Injectable({ providedIn: 'root' })
export class DescriptionService {
  public resourceUrl = SERVER_API_URL + 'api/descriptions';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/descriptions';

  constructor(protected http: HttpClient) {}

  create(description: IDescription): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(description);
    return this.http
      .post<IDescription>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(description: IDescription): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(description);
    return this.http
      .put<IDescription>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<IDescription>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDescription[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDescription[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(description: IDescription): IDescription {
    const copy: IDescription = Object.assign({}, description, {
      orderDate: description.orderDate != null && description.orderDate.isValid() ? description.orderDate.toJSON() : null,
      deleveryDate: description.deleveryDate != null && description.deleveryDate.isValid() ? description.deleveryDate.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.orderDate = res.body.orderDate != null ? moment(res.body.orderDate) : null;
      res.body.deleveryDate = res.body.deleveryDate != null ? moment(res.body.deleveryDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((description: IDescription) => {
        description.orderDate = description.orderDate != null ? moment(description.orderDate) : null;
        description.deleveryDate = description.deleveryDate != null ? moment(description.deleveryDate) : null;
      });
    }
    return res;
  }
}
