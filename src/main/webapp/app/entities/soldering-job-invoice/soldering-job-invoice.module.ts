import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { RoyOpticalsSharedModule } from 'app/shared/shared.module';
import { SolderingJobInvoiceComponent } from './soldering-job-invoice.component';
import { SolderingJobInvoiceDetailComponent } from './soldering-job-invoice-detail.component';
import { SolderingJobInvoiceUpdateComponent } from './soldering-job-invoice-update.component';
import {
  SolderingJobInvoiceDeletePopupComponent,
  SolderingJobInvoiceDeleteDialogComponent
} from './soldering-job-invoice-delete-dialog.component';
import { solderingJobInvoiceRoute, solderingJobInvoicePopupRoute } from './soldering-job-invoice.route';

const ENTITY_STATES = [...solderingJobInvoiceRoute, ...solderingJobInvoicePopupRoute];

@NgModule({
  imports: [RoyOpticalsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    SolderingJobInvoiceComponent,
    SolderingJobInvoiceDetailComponent,
    SolderingJobInvoiceUpdateComponent,
    SolderingJobInvoiceDeleteDialogComponent,
    SolderingJobInvoiceDeletePopupComponent
  ],
  entryComponents: [SolderingJobInvoiceDeleteDialogComponent]
})
export class RoyOpticalsSolderingJobInvoiceModule {}
