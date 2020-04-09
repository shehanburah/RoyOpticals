import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { PettyCashStock } from 'app/shared/model/petty-cash-stock.model';
import { PettyCashStockService } from './petty-cash-stock.service';
import { PettyCashStockComponent } from './petty-cash-stock.component';
import { PettyCashStockDetailComponent } from './petty-cash-stock-detail.component';
import { PettyCashStockUpdateComponent } from './petty-cash-stock-update.component';
import { PettyCashStockDeletePopupComponent } from './petty-cash-stock-delete-dialog.component';
import { IPettyCashStock } from 'app/shared/model/petty-cash-stock.model';

@Injectable({ providedIn: 'root' })
export class PettyCashStockResolve implements Resolve<IPettyCashStock> {
  constructor(private service: PettyCashStockService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPettyCashStock> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((pettyCashStock: HttpResponse<PettyCashStock>) => pettyCashStock.body));
    }
    return of(new PettyCashStock());
  }
}

export const pettyCashStockRoute: Routes = [
  {
    path: '',
    component: PettyCashStockComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'PettyCashStocks'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PettyCashStockDetailComponent,
    resolve: {
      pettyCashStock: PettyCashStockResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PettyCashStocks'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PettyCashStockUpdateComponent,
    resolve: {
      pettyCashStock: PettyCashStockResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PettyCashStocks'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PettyCashStockUpdateComponent,
    resolve: {
      pettyCashStock: PettyCashStockResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PettyCashStocks'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const pettyCashStockPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: PettyCashStockDeletePopupComponent,
    resolve: {
      pettyCashStock: PettyCashStockResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PettyCashStocks'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
