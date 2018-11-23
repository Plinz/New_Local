import { Component, AfterViewInit, Renderer, ElementRef } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { JhiEventManager } from 'ng-jhipster';

@Component({
    selector: 'jhi-popup',
    templateUrl: './popup.component.html',
    styles: []
})
export class PopupComponent {
    constructor(private router: Router, public activeModal: NgbActiveModal) {}
}
