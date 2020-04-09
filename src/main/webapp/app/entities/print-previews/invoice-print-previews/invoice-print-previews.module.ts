import { NgModule } from '@angular/core';
import { InvoicePrintPreviewsComponent } from './invoice-print-previews.component';
// import { NewRoyOpticalsSharedModule } from 'app/shared/shared';
import { RouterModule } from '@angular/router';
import { RoyOpticalsSharedModule } from 'app/shared/shared.module';
import { invoicePrintRoute } from '../print-previews.route';
// import { invoicePrintRoute } from '../print-previews.route';

@NgModule({
    declarations: [
        InvoicePrintPreviewsComponent
      ],
    // declarations: [
    //   InvoicePrintPreviewsComponent
    // ],
    entryComponents:[
        InvoicePrintPreviewsComponent
    ],
    imports: [
       RoyOpticalsSharedModule,
       RouterModule.forChild(invoicePrintRoute)
    ],
  
  })
  export class InvoicePrintPreviewsModule { }
  