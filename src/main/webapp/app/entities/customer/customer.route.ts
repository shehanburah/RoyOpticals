import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Customer } from 'app/shared/model/customer.model';
import { CustomerService } from './customer.service';
import { CustomerComponent } from './customer.component';
import { CustomerDetailComponent } from './customer-detail.component';
import { CustomerUpdateComponent } from './customer-update.component';
import { CustomerDeletePopupComponent } from './customer-delete-dialog.component';
import { ICustomer } from 'app/shared/model/customer.model';

@Injectable({ providedIn: 'root' })
export class CustomerResolve implements Resolve<ICustomer> {
  constructor(private service: CustomerService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICustomer> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((customer: HttpResponse<Customer>) => customer.body));
    }
    return of(new Customer());
  }
}

export const customerRoute: Routes = [
  {
    path: '',
    component: CustomerComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,desc',
      pageTitle: 'Customers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CustomerDetailComponent,
    resolve: {
      customer: CustomerResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Customers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CustomerUpdateComponent,
    resolve: {
      customer: CustomerResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Customers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CustomerUpdateComponent,
    resolve: {
      customer: CustomerResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Customers'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const customerPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: CustomerDeletePopupComponent,
    resolve: {
      customer: CustomerResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Customers'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
