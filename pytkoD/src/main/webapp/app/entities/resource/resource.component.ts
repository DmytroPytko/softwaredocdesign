import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IResource } from 'app/shared/model/resource.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ResourceService } from './resource.service';
import { ResourceDeleteDialogComponent } from './resource-delete-dialog.component';

@Component({
  selector: 'jhi-resource',
  templateUrl: './resource.component.html'
})
export class ResourceComponent implements OnInit, OnDestroy {
  resources: IResource[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected resourceService: ResourceService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.resources = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.resourceService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IResource[]>) => this.paginateResources(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.resources = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInResources();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IResource): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInResources(): void {
    this.eventSubscriber = this.eventManager.subscribe('resourceListModification', () => this.reset());
  }

  delete(resource: IResource): void {
    const modalRef = this.modalService.open(ResourceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.resource = resource;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateResources(data: IResource[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.resources.push(data[i]);
      }
    }
  }
}
