import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { IDescription, Description } from 'app/shared/model/description.model';
import { DescriptionService } from '../description/description.service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { DescriptionPrintPreviewsComponent } from './description-print-previews/description-print-previews.component';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { InvoicePrintPreviewsComponent } from './invoice-print-previews/invoice-print-previews.component';
import { IInvoice, Invoice } from 'app/shared/model/invoice.model';
import { InvoiceService } from '../invoice/invoice.service';

@Injectable({ providedIn: 'root' })
export class InvoiceResolve implements Resolve<IInvoice> {
  constructor(private service: InvoiceService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IInvoice> {
    const id = route.params['id'];
    if (id) {
      this.service.find(id).subscribe(res => {});
      return this.service
        .find(id)

        .pipe(
          filter((response: HttpResponse<Invoice>) => response.ok),
          map((invoice: HttpResponse<Invoice>) => invoice.body)
        );
    }
    return of(new Invoice());
  }
}

@Injectable({ providedIn: 'root' })
export class DescriptionResolve implements Resolve<IDescription> {
  constructor(private service: DescriptionService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IDescription> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Description>) => response.ok),
        map((description: HttpResponse<Description>) => description.body)
      );
    }
    return of(new Description());
  }
}

export const printRoute: Routes = [
  {
    path: '',
    component: DescriptionPrintPreviewsComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Description'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'description/:id',
    component: DescriptionPrintPreviewsComponent,
    resolve: {
      description: DescriptionResolve
    },

    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Description'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const invoicePrintRoute: Routes = [
  {
    path: '',
    component: InvoicePrintPreviewsComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Invoice'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'invoice/:id',
    component: InvoicePrintPreviewsComponent,
    resolve: {
      invoice: InvoiceResolve
    },

    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Invoice'
    },
    canActivate: [UserRouteAccessService]
  }
];
