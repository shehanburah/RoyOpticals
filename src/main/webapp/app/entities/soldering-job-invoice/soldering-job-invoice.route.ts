import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { SolderingJobInvoice } from 'app/shared/model/soldering-job-invoice.model';
import { SolderingJobInvoiceService } from './soldering-job-invoice.service';
import { SolderingJobInvoiceComponent } from './soldering-job-invoice.component';
import { SolderingJobInvoiceDetailComponent } from './soldering-job-invoice-detail.component';
import { SolderingJobInvoiceUpdateComponent } from './soldering-job-invoice-update.component';
import { SolderingJobInvoiceDeletePopupComponent } from './soldering-job-invoice-delete-dialog.component';
import { ISolderingJobInvoice } from 'app/shared/model/soldering-job-invoice.model';

@Injectable({ providedIn: 'root' })
export class SolderingJobInvoiceResolve implements Resolve<ISolderingJobInvoice> {
  constructor(private service: SolderingJobInvoiceService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISolderingJobInvoice> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((solderingJobInvoice: HttpResponse<SolderingJobInvoice>) => solderingJobInvoice.body));
    }
    return of(new SolderingJobInvoice());
  }
}

export const solderingJobInvoiceRoute: Routes = [
  {
    path: '',
    component: SolderingJobInvoiceComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'SolderingJobInvoices'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: SolderingJobInvoiceDetailComponent,
    resolve: {
      solderingJobInvoice: SolderingJobInvoiceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SolderingJobInvoices'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: SolderingJobInvoiceUpdateComponent,
    resolve: {
      solderingJobInvoice: SolderingJobInvoiceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SolderingJobInvoices'
    },
    canActivate: [UserRouteAccessService]
  },  {
    path: ':customerId/new',
    component: SolderingJobInvoiceUpdateComponent,
    resolve: {
      solderingJobInvoice: SolderingJobInvoiceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SolderingJobInvoices'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: SolderingJobInvoiceUpdateComponent,
    resolve: {
      solderingJobInvoice: SolderingJobInvoiceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SolderingJobInvoices'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const solderingJobInvoicePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: SolderingJobInvoiceDeletePopupComponent,
    resolve: {
      solderingJobInvoice: SolderingJobInvoiceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SolderingJobInvoices'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
