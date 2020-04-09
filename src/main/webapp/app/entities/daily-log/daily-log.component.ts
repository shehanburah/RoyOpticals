import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse, HttpParams } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { IDailyLog, DailyLog } from 'app/shared/model/daily-log.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { DailyLogService } from './daily-log.service';
import { FormBuilder } from '@angular/forms';
import { Moment } from 'moment';
import * as moment from 'moment';
import * as XLSX from 'xlsx';
import { AccountService } from 'app/core/auth/account.service';

import { Account } from 'app/core/user/account.model';
import { MonthlyLogService } from '../monthly-log/monthly-log.service';
import { MonthlyLog } from 'app/shared/model/monthly-log.model';
export interface IDailyLog2 {
  id?: string;
  createdDate?: string;
  rx?: number;
  invoiceId?: string;
  solderingJobInvoiceId?: string;
  customer?: string;
  customerId?: string;
  description?: string;
  amount?: number;
}

export class DailyLog2 implements IDailyLog2 {
  constructor(
    public id?: string,
    public createdDate?: string,
    public rx?: number,
    public invoiceId?: string,
    public solderingJobInvoiceId?: string,
    public customer?: string,
    public customerId?: string,
    public description?: string,
    public amount?: number
  ) { }
}

@Component({
  selector: 'jhi-daily-log',
  templateUrl: './daily-log.component.html',
  styleUrls: ['./daily-log.component.scss']
})
export class DailyLogComponent implements OnInit, OnDestroy {

  monthlyReportStruct = {
    id: '',
    'rx/identification': 0,
    reportObtainedBy: '',
    description: '',
    amount: 0,
    createdDate: ''
  }
  monthlyReportStructArray = []

  dailyLogs: IDailyLog[];
  error: any;
  success: any;
  eventSubscriber: Subscription;
  currentSearch: string;
  routeData: any;
  links: any;
  totalItems: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  previousPage: any;
  reverse: any;

  editForm = this.fb.group({
    generateReportDate: [],
  });

  calcBalanceForm = this.fb.group({
    amountBF: [],
    amountToHand: [],
    bfDate: [],
    createdDate: []
  })

  calcMonthlyForm = this.fb.group({
    year: [],
    month: []
  });


  todayReport: IDailyLog[];
  todayReportRecordsCount = 0;
  advanceCount = 0;
  balanceCount = 0;
  fullPaidCount = 0;
  totalRevenue = 0;
  totalRevenueLocalString = 'Rs. 00.00';
  solderingJobCount = 0;
  currentUser: Account;
  bfAmount = 0;
  avoid2ndBF = '';
  monthlyReportRecordsCount = 0;
  searchRx: any;
  withdrawMonthlyTotal = 0;
  monthlyReport: IDailyLog[];
  withdrawMonthlyTotalString = '00.00';
  constructor(
    protected dailyLogService: DailyLogService,
    protected parseLinks: JhiParseLinks,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    private fb: FormBuilder,
    private accountService: AccountService,
    private monthlyLogService: MonthlyLogService
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.routeData = this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.previousPage = data.pagingParams.page;
      this.reverse = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
    });
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll() {
    if (this.currentSearch) {
      this.dailyLogService
        .search({
          page: this.page - 1,
          query: this.currentSearch,
          size: this.itemsPerPage,
          sort: this.sort()
        })
        .subscribe((res: HttpResponse<IDailyLog[]>) => this.paginateDailyLogs(res.body, res.headers));
      return;
    }
    this.dailyLogService
      .query({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IDailyLog[]>) => this.paginateDailyLogs(res.body, res.headers));
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/daily-log'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        search: this.currentSearch,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    });
    this.loadAll();
  }

  clear() {
    this.page = 0;
    this.currentSearch = '';
    this.router.navigate([
      '/daily-log',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  search(query) {
    if (!query) {
      return this.clear();
    }
    this.page = 0;
    this.currentSearch = query;
    this.router.navigate([
      '/daily-log',
      {
        search: this.currentSearch,
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  ngOnInit() {
    this.loadAll();
    this.registerChangeInDailyLogs();
    this.accountService.fetch().subscribe(res => {
      this.currentUser = res;
    })

  }
  onAmountInHandChanged() {
    this.calcBalanceForm.patchValue({
      amountBF: this.totalRevenue - this.calcBalanceForm.get(['amountToHand']).value
    });
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IDailyLog) {
    return item.id;
  }

  registerChangeInDailyLogs() {
    this.eventSubscriber = this.eventManager.subscribe('dailyLogListModification', () => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateDailyLogs(data: IDailyLog[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.dailyLogs = data;
  }

  onDateSelected() {
    // alert(this.editForm.get(['generateReportDate']).value)
  }
  viewReportSummary() {
    const date: string = moment(this.editForm.get(['generateReportDate']).value).format('YYYY-MM-DD');



    const params = new HttpParams()
      .set('createdDate', date)
    // 
    this.dailyLogService.search({ query: 'createdDate:' + date }).subscribe(res => {
      this.todayReport = res.body;
      this.todayReportRecordsCount = res.body.length;
      this.totalRevenue = 0;
      this.fullPaidCount = 0
      this.advanceCount = 0
      this.balanceCount = 0
      this.solderingJobCount = 0
      res.body.forEach(d => {
        if (d.description === 'FULL_PAID' || d.description === 'ADVANCE' || d.description === 'BALANCE' || d.description === 'BROUGHT_FORWARD') {


          if (d.description === 'FULL_PAID' || d.description === 'ADVANCE' || d.description === 'BALANCE') {
            this.totalRevenue += d.amount
          } else if (d.description === 'BROUGHT_FORWARD' && this.avoid2ndBF !== d.description) {
            this.totalRevenue += d.amount
            this.avoid2ndBF = 'BROUGHT_FORWARD';
          }
          if (this.avoid2ndBF === d.description) {
            alert('Multiple B/F detected in the system and the first record will take in to consideration')
          }




          // if (this.avoid2ndBF !== 'BROUGHT_FORWARD') {

          //   this.totalRevenue += d.amount
          // } else {
          //   alert('Duplicated B/F detected in the system')
          // }
        }

        if (d.description === 'FULL_PAID') {
          this.fullPaidCount += 1;
        } else if (d.description === 'ADVANCE') {
          this.advanceCount += 1;
        } else if (d.description === 'BALANCE') {
          this.balanceCount += 1;
        } else if (d.description === 'SOLDERING_FULL_PAID') {
          this.solderingJobCount += 1;
        } else if (d.description === 'BROUGHT_FORWARD') {
          this.bfAmount = d.amount
        }
      })
      this.totalRevenueLocalString = 'Rs.' + this.totalRevenue.toLocaleString()
    });


  }

  ExportTOExcel() {
    const report: any = [];
    this.todayReport.forEach((d, i) => {
      const item = new DailyLog2();
      item.id = d.id;
      item.amount = d.amount;
      item.rx = d.rx;
      item.invoiceId = d.invoiceId;
      item.solderingJobInvoiceId = d.solderingJobInvoiceId;
      item.customer = d.customer;
      item.customerId = d.customerId;
      item.description = d.description;
      item.createdDate = moment(d.createdDate).format('YYYY-MM-DD');
      report.push(item);

    })
    const date: string = moment(this.editForm.get(['generateReportDate']).value).format('YYYY-MM-DD');
    const ws: XLSX.WorkSheet = XLSX.utils.json_to_sheet(report)
    const wb: XLSX.WorkBook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, date);
    XLSX.writeFile(wb, 'Negombo_DailyReport_' + date + '.xlsx');
  }

  calcbalance() {
    const recordBF = new DailyLog();
    const withdraw = new DailyLog();

    withdraw.description = 'WITHDRAW';
    withdraw.amount = this.calcBalanceForm.get(['amountToHand']).value
    withdraw.createdDate = this.calcBalanceForm.get(['createdDate']).value
    withdraw.customer = this.currentUser.login
    withdraw.rx = Number(moment().format('YYYYMM'));

    recordBF.description = 'BROUGHT_FORWARD'
    recordBF.amount = this.calcBalanceForm.get(['amountBF']).value
    recordBF.createdDate = this.calcBalanceForm.get(['bfDate']).value
    recordBF.customer = this.currentUser.login

    recordBF.rx = Number(moment().format('YYYYMM'));

    this.dailyLogService.create(withdraw).subscribe(() => {
      this.dailyLogService.create(recordBF).subscribe(() => {
        alert('Successful! Page will be refreshed')
        location.reload();
      })
    })

  }

  viewMonthlyReport() {
    // alert(this.calcMonthlyForm.get(['year']).value + this.calcMonthlyForm.get(['month']).value)
    this.searchRx = this.calcMonthlyForm.get(['year']).value + this.calcMonthlyForm.get(['month']).value;
    const query = new URLSearchParams();
    // query.append()
    this.dailyLogService.search({ query: 'rx:' + this.searchRx }).subscribe(res => {
      this.monthlyReport = res.body;
      res.body.forEach(d => {
        if (d.description === 'WITHDRAW') {
          // 
          this.withdrawMonthlyTotal += d.amount
          this.withdrawMonthlyTotalString = this.withdrawMonthlyTotal.toLocaleString();
        } else {
          // 
        }
      });




    });

    this.currentSearch = this.searchRx;
    this.search(this.currentSearch)
  }
  ExportMonthlyTOExcel() {
  
    this.monthlyReport.forEach(d => {
      if (d.description === 'WITHDRAW') {
        const item = this.monthlyReportStruct;
        item["rx/identification"] = d.rx;
        item.amount = d.amount;
        item.reportObtainedBy = d.customer;
        item.id = d.id;
        item.description = d.description;
        item.createdDate = moment(d.createdDate).format('YYYY-MM-DD');
        this.monthlyReportStructArray.push(item);

      }
    });

    const date: string = moment().format('YYYY-MM-DD');
    const ws: XLSX.WorkSheet = XLSX.utils.json_to_sheet(this.monthlyReportStructArray)
    const wb: XLSX.WorkBook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, date);
    XLSX.writeFile(wb, 'Negombo_MonthlyReport_' + date + '.xlsx');

  }
  saveToMonthlyLog(){
    const monthlyLog = new MonthlyLog();
    monthlyLog.amount = this.withdrawMonthlyTotal;
    monthlyLog.createdDate = moment();
    monthlyLog.identification = this.searchRx;
    monthlyLog.reportObtainedBy = this.currentUser.login;
    this.monthlyLogService.create(monthlyLog).subscribe(r => {

    })
  }
}
