import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'customer',
        loadChildren: () => import('./customer/customer.module').then(m => m.RoyOpticalsCustomerModule)
      },
      {
        path: 'invoice',
        loadChildren: () => import('./invoice/invoice.module').then(m => m.RoyOpticalsInvoiceModule)
      },
      {
        path: 'items',
        loadChildren: () => import('./items/items.module').then(m => m.RoyOpticalsItemsModule)
      },
      {
        path: 'prescription',
        loadChildren: () => import('./prescription/prescription.module').then(m => m.RoyOpticalsPrescriptionModule)
      },
      {
        path: 'description',
        loadChildren: () => import('./description/description.module').then(m => m.RoyOpticalsDescriptionModule)
      },
      {
        path: 'print',
        loadChildren: () =>
          import('./print-previews/description-print-previews/description-print-previews.module').then(
            m => m.DescriptionPrintPreviewsModule
          )
      },
      {
        path: 'print',
        loadChildren: () =>
          import('./print-previews/invoice-print-previews/invoice-print-previews.module').then(m => m.InvoicePrintPreviewsModule)
      },
      {
        path: 'petty-cash-stock',
        loadChildren: () => import('./petty-cash-stock/petty-cash-stock.module').then(m => m.RoyOpticalsPettyCashStockModule)
      },
      {
        path: 'soldering-job-invoice',
        loadChildren: () => import('./soldering-job-invoice/soldering-job-invoice.module').then(m => m.RoyOpticalsSolderingJobInvoiceModule)
      },
      {
        path: 'daily-log',
        loadChildren: () => import('./daily-log/daily-log.module').then(m => m.RoyOpticalsDailyLogModule)
      },
      {
        path: 'monthly-log',
        loadChildren: () => import('./monthly-log/monthly-log.module').then(m => m.RoyOpticalsMonthlyLogModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class RoyOpticalsEntityModule {}
