import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IContractType } from 'app/shared/model/contract-type.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ContractTypeService } from './contract-type.service';
import { ContractTypeDeleteDialogComponent } from './contract-type-delete-dialog.component';

@Component({
  selector: 'jhi-contract-type',
  templateUrl: './contract-type.component.html'
})
export class ContractTypeComponent implements OnInit, OnDestroy {
  contractTypes: IContractType[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected contractTypeService: ContractTypeService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.contractTypes = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.contractTypeService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IContractType[]>) => this.paginateContractTypes(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.contractTypes = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInContractTypes();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IContractType): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInContractTypes(): void {
    this.eventSubscriber = this.eventManager.subscribe('contractTypeListModification', () => this.reset());
  }

  delete(contractType: IContractType): void {
    const modalRef = this.modalService.open(ContractTypeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.contractType = contractType;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateContractTypes(data: IContractType[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.contractTypes.push(data[i]);
      }
    }
  }
}
