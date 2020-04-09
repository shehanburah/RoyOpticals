import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Items } from 'app/shared/model/items.model';
import { ItemsService } from './items.service';
import { ItemsComponent } from './items.component';
import { ItemsDetailComponent } from './items-detail.component';
import { ItemsUpdateComponent } from './items-update.component';
import { ItemsDeletePopupComponent } from './items-delete-dialog.component';
import { IItems } from 'app/shared/model/items.model';

@Injectable({ providedIn: 'root' })
export class ItemsResolve implements Resolve<IItems> {
  constructor(private service: ItemsService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IItems> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((items: HttpResponse<Items>) => items.body));
    }
    return of(new Items());
  }
}

export const itemsRoute: Routes = [
  {
    path: '',
    component: ItemsComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Items'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ItemsDetailComponent,
    resolve: {
      items: ItemsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Items'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ItemsUpdateComponent,
    resolve: {
      items: ItemsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Items'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ItemsUpdateComponent,
    resolve: {
      items: ItemsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Items'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const itemsPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ItemsDeletePopupComponent,
    resolve: {
      items: ItemsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Items'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
