import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Description } from 'app/shared/model/description.model';
import { DescriptionService } from './description.service';
import { DescriptionComponent } from './description.component';
import { DescriptionDetailComponent } from './description-detail.component';
import { DescriptionUpdateComponent } from './description-update.component';
import { DescriptionDeletePopupComponent } from './description-delete-dialog.component';
import { IDescription } from 'app/shared/model/description.model';

@Injectable({ providedIn: 'root' })
export class DescriptionResolve implements Resolve<IDescription> {
  constructor(private service: DescriptionService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDescription> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((description: HttpResponse<Description>) => description.body));
    }
    return of(new Description());
  }
}

export const descriptionRoute: Routes = [
  {
    path: '',
    component: DescriptionComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Descriptions'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DescriptionDetailComponent,
    resolve: {
      description: DescriptionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Descriptions'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DescriptionUpdateComponent,
    resolve: {
      description: DescriptionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Descriptions'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':customerId/new',
    component: DescriptionUpdateComponent,
    resolve: {
      description: DescriptionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Descriptions'
    },
    canActivate: [UserRouteAccessService]
  },

  {
    path: ':customerId/:invoiceId/new',
    component: DescriptionUpdateComponent,
    resolve: {
      description: DescriptionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Descriptions'
    },
    canActivate: [UserRouteAccessService]
  },


  {
    path: ':id/edit',
    component: DescriptionUpdateComponent,
    resolve: {
      description: DescriptionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Descriptions'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const descriptionPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: DescriptionDeletePopupComponent,
    resolve: {
      description: DescriptionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Descriptions'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
